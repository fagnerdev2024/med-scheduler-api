import { DadosEndereco } from './endereco.model';

export type Especialidade =
  | 'ORTOPEDIA - Tratamento de condições do sistema musculoesquelético, incluindo ossos, músculos e articulações.'
  | 'CARDIOLOGIA - Diagnóstico e tratamento de doenças do coração e do sistema cardiovascular.'
  | 'GINECOLOGIA - Cuidados médicos relacionados à saúde feminina, incluindo o sistema reprodutivo.'
  | 'DERMATOLOGIA - Estudo e tratamento de condições relacionadas à pele, cabelos e unhas.'
  | 'NEUROLOGIA - Diagnóstico e tratamento de distúrbios do sistema nervoso, incluindo cérebro e medula espinhal.'
  | 'PEDIATRIA - Cuidados médicos voltados para crianças e adolescentes, abrangendo crescimento e desenvolvimento.';

export interface DadosCadastroMedico {
  nome: string;
  email: string;
  telefone: string;
  crm: string;
  especialidade: Especialidade;
  endereco: DadosEndereco;
}

export interface DadosAtualizacaoMedicoRequest {
  nome?: string;
  telefone?: string;
  endereco?: DadosEndereco;
}

export interface DadosListagemMedico {
  id: number;
  nome: string;
  email: string;
  crm: string;
  especialidade: string;
  ativo: boolean;
}

export interface DadosDetalhamentoMedico extends DadosListagemMedico {
  telefone: string;
  endereco: DadosEndereco;
}
