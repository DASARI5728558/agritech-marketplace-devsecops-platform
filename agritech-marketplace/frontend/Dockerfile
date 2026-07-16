# Static AgriTech Marketplace frontend, served by nginx.
# API calls to /api/ are proxied to the backend service (see nginx/default.conf).
# NOTE: build context is the repository root (see docker-compose.yml / CI),
# so paths below are relative to the repo root, not this file's directory.
FROM nginx:1.27-alpine

RUN rm -rf /usr/share/nginx/html/*
COPY frontend/ /usr/share/nginx/html/
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget -qO- http://localhost/health || exit 1
