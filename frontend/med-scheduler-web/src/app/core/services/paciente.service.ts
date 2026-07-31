import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Page } from '../models/page.model';
import {
  DadosCadastroPaciente,
  DadosAtualizacaoPacienteRequest,
  DadosDetalhamentoPaciente,
  DadosListagemPaciente
} from '../models/paciente.model';

@Injectable({
  providedIn: 'root'
})
export class PacienteService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = environment.apiUrl;

  listar(page = 0, size = 10, sort = 'nome,ASC'): Observable<Page<DadosListagemPaciente>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<DadosListagemPaciente>>(`${this.apiUrl}/pacientes`, { params });
  }

  detalhar(id: number): Observable<DadosDetalhamentoPaciente> {
    return this.http.get<DadosDetalhamentoPaciente>(`${this.apiUrl}/pacientes/${id}`);
  }

  cadastrar(dados: DadosCadastroPaciente): Observable<DadosDetalhamentoPaciente> {
    return this.http.post<DadosDetalhamentoPaciente>(`${this.apiUrl}/pacientes`, dados);
  }

  atualizar(id: number, dados: DadosAtualizacaoPacienteRequest): Observable<DadosDetalhamentoPaciente> {
    return this.http.put<DadosDetalhamentoPaciente>(`${this.apiUrl}/pacientes/${id}`, dados);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/pacientes/${id}`);
  }
}
