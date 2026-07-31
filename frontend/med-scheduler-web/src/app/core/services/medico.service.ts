import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../models/page.model';
import {
  DadosCadastroMedico,
  DadosAtualizacaoMedicoRequest,
  DadosDetalhamentoMedico,
  DadosListagemMedico
} from '../models/medico.model';

@Injectable({
  providedIn: 'root'
})
export class MedicoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  listar(page = 0, size = 10, sort = 'nome,ASC'): Observable<Page<DadosListagemMedico>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<DadosListagemMedico>>(`${this.apiUrl}/medicos`, { params });
  }

  detalhar(id: number): Observable<DadosDetalhamentoMedico> {
    return this.http.get<DadosDetalhamentoMedico>(`${this.apiUrl}/medicos/${id}`);
  }

  cadastrar(dados: DadosCadastroMedico): Observable<DadosDetalhamentoMedico> {
    return this.http.post<DadosDetalhamentoMedico>(`${this.apiUrl}/medicos`, dados);
  }

  atualizar(id: number, dados: DadosAtualizacaoMedicoRequest): Observable<DadosDetalhamentoMedico> {
    return this.http.put<DadosDetalhamentoMedico>(`${this.apiUrl}/medicos/${id}`, dados);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/medicos/${id}`);
  }
}
