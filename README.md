# p2pzestdemo

This repo contains the frontend app and the active backend implementation for the ZestP2P scheduling platform.

## Application

- Frontend: React + TypeScript in [src](src), served by Nginx in its own container.
- Backend: Spring Boot in [springbackend](springbackend), served in a separate Java container.
- [compose.yaml](compose.yaml) runs both services. Nginx serves the frontend and proxies `/api` to the backend; only the frontend publishes host port `8080`.

## CI/CD

The [CI and EC2 deploy workflow](.github/workflows/deploy.yml) lints and builds both applications, publishes two images to GitHub Container Registry, then deploys them on the EC2 self-hosted runner with Docker Compose. Each `main` deployment pulls the commit-tagged images and recreates both containers. The first deployment creates them if they do not exist.

One-time EC2 requirements:

- Install Docker Engine with the Compose plugin, and allow the `ubuntu` runner account to use Docker.
- Keep `/etc/zestp2p/zestp2p.env` on the instance; Compose passes it to the backend container. Do not commit credentials.
- Allow inbound TCP `8080` in the instance security group if the app should be reachable externally. Use `http://<EC2-public-IP>:8080/`; TLS is not configured here.
- For persistent production data, configure PostgreSQL (for example, RDS) in the environment file. The current default H2 database is in-memory.

The legacy `zestp2p.service` systemd service is stopped and disabled by the first Compose deployment to free port `8080`.
