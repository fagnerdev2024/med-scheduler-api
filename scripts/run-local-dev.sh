#!/bin/bash
set -e

# Executa a aplicação local apontando para o banco de dev (Neon).
# Crie o arquivo .env.local-dev a partir de .env.example e preencha com os dados do banco.

if [ ! -f .env.local-dev ]; then
  echo "Arquivo .env.local-dev não encontrado."
  echo "Copie .env.example para .env.local-dev e preencha as variáveis de ambiente."
  exit 1
fi

set -a
source .env.local-dev
set +a

./gradlew bootRun --args="--spring.profiles.active=local-dev"
