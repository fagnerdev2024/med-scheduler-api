export type MotivoCancelamento =
  | 'PACIENTE_DESISTIU - Paciente desistiu'
  | 'MEDICO_CANCELOU - Médico cancelou'
  | 'OUTROS - Outros motivos';

export interface DadosAgendamentoConsulta {
  idMedico?: number | null;
  idPaciente: number;
  data: string; // ISO-8601
  especialidade?: string | null;
}

export interface DadosCancelamentoConsulta {
  idConsulta: number;
  motivo: MotivoCancelamento;
}

export interface DadosDetalhamentoConsulta {
  id: number;
  idMedico: number;
  idPaciente: number;
  data: string;
  motivoCancelamento?: MotivoCancelamento | null;
}
