import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../models/page.model';
import {
  DadosAgendamentoConsulta,
  DadosCancelamentoConsulta,
  DadosDetalhamentoConsulta
} from '../models/consulta.model';

@Injectable({
  providedIn: 'root'
})
export class ConsultaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  listar(page = 0, size = 10, sort = 'data,ASC'): Observable<Page<DadosDetalhamentoConsulta>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<DadosDetalhamentoConsulta>>(`${this.apiUrl}/consultas`, { params });
  }

  agendar(dados: DadosAgendamentoConsulta): Observable<DadosDetalhamentoConsulta> {
    return this.http.post<DadosDetalhamentoConsulta>(`${this.apiUrl}/consultas`, dados);
  }

  cancelar(dados: DadosCancelamentoConsulta): Observable<void> {
    return this.http.request<void>('delete', `${this.apiUrl}/consultas`, { body: dados });
  }
}
