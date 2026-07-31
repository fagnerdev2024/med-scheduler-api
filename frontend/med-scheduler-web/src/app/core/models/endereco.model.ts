export interface DadosEndereco {
  logradouro: string;
  bairro: string;
  cep: string;
  cidade: string;
  uf: string;
  numero?: string | null;
  complemento?: string | null;
}
