import { DadosEndereco } from './endereco.model';

export interface DadosCadastroPaciente {
  nome: string;
  email: string;
  telefone: string;
  cpf: string;
  endereco: DadosEndereco;
}

export interface DadosAtualizacaoPacienteRequest {
  nome?: string;
  telefone?: string;
  endereco?: DadosEndereco;
}

export interface DadosListagemPaciente {
  id: number;
  nome: string;
  email: string;
  cpf: string;
  ativo: boolean;
}

export interface DadosDetalhamentoPaciente extends DadosListagemPaciente {
  telefone: string;
  endereco: DadosEndereco;
}
