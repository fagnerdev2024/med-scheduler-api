#!/usr/bin/env bash
set -e

if [ -z "$1" ]; then
    echo "Uso: ./scripts/new-feature.sh <nome-da-feature>"
    echo "Exemplo: ./scripts/new-feature.sh ajuste-validacao"
    exit 1
fi

FEATURE_NAME="$1"
FEATURE_BRANCH="feature/${FEATURE_NAME}"

echo "Atualizando develop..."
git fetch origin
git checkout develop
git reset --hard origin/develop

echo "Criando branch ${FEATURE_BRANCH} a partir da develop atualizada..."
git checkout -b "${FEATURE_BRANCH}"

echo "Pronto. Voce esta na branch ${FEATURE_BRANCH}."
