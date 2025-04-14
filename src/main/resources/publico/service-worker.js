const CACHE_NAME = 'acortador-cache-v1';
const OFFLINE_URL = '/offline.html';

const urlsToCache = [
    '/',
    '/index.html',
    '/login.html',
    '/registro.html',
    '/dashboard.html',
    '/offline.html',
    '/styleIndex.css',
    '/auth.js',
    '/promote.html',
    '/service-worker.js',
    // Agrega otros recursos estáticos necesarios
];

self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                console.log('Cache abierto');
                return cache.addAll(urlsToCache);
            })
    );
});

self.addEventListener('fetch', event => {
    const requestUrl = new URL(event.request.url);

    // Si es una petición a la API de vista previa
    if (requestUrl.pathname.startsWith('/preview')) {
        event.respondWith(
            fetch(event.request).catch(() => {
                const fallbackData = {
                    data: {
                        title: "Sin conexión",
                        description: "No es posible obtener vista previa sin conexión.",
                        image: { url: "" }
                    }
                };
                return new Response(JSON.stringify(fallbackData), {
                    headers: { 'Content-Type': 'application/json' }
                });
            })
        );
        return;
    }

    // Si es una petición a estadísticas o listado de URLs
    if (requestUrl.pathname.startsWith('/dashboardStats') || requestUrl.pathname.startsWith('/listUrls')) {
        event.respondWith(
            fetch(event.request).catch(() => {
                let fallback;
                if (requestUrl.pathname.startsWith('/dashboardStats')) {
                    fallback = { totalUrls: 0, totalHits: 0, hitsByDay: {}, hitsByBrowser: {}, hitsByOs: {} };
                } else {
                    fallback = []; // Vacío para listUrls
                }
                return new Response(JSON.stringify(fallback), {
                    headers: { 'Content-Type': 'application/json' }
                });
            })
        );
        return;
    }

    // Para peticiones de navegación (cuando el usuario ingresa una URL en la barra)
    if (event.request.mode === 'navigate') {
        event.respondWith(
            fetch(event.request).catch(() => caches.match(OFFLINE_URL))
        );
        return;
    }

    // Para otros recursos, se usa una estrategia cache-first
    event.respondWith(
        caches.match(event.request)
            .then(response => response || fetch(event.request))
    );
});

self.addEventListener('activate', event => {
    const cacheWhitelist = [CACHE_NAME];
    event.waitUntil(
        caches.keys()
            .then(cacheNames => {
                return Promise.all(
                    cacheNames.map(cacheName => {
                        if (!cacheWhitelist.includes(cacheName)) {
                            return caches.delete(cacheName);
                        }
                    })
                );
            })
    );
});
