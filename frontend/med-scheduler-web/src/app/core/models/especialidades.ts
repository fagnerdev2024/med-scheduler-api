import { Especialidade } from './medico.model';

export const ESPECIALIDADES: { valor: Especialidade; label: string }[] = [
  {
    valor: 'ORTOPEDIA - Tratamento de condições do sistema musculoesquelético, incluindo ossos, músculos e articulações.',
    label: 'Ortopedia'
  },
  {
    valor: 'CARDIOLOGIA - Diagnóstico e tratamento de doenças do coração e do sistema cardiovascular.',
    label: 'Cardiologia'
  },
  {
    valor: 'GINECOLOGIA - Cuidados médicos relacionados à saúde feminina, incluindo o sistema reprodutivo.',
    label: 'Ginecologia'
  },
  {
    valor: 'DERMATOLOGIA - Estudo e tratamento de condições relacionadas à pele, cabelos e unhas.',
    label: 'Dermatologia'
  },
  {
    valor: 'NEUROLOGIA - Diagnóstico e tratamento de distúrbios do sistema nervoso, incluindo cérebro e medula espinhal.',
    label: 'Neurologia'
  },
  {
    valor: 'PEDIATRIA - Cuidados médicos voltados para crianças e adolescentes, abrangendo crescimento e desenvolvimento.',
    label: 'Pediatria'
  }
];
