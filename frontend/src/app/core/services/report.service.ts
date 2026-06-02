import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReportSummary } from '../models/report.model';

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly api = `${environment.apiUrl}/reports`;

  constructor(private readonly http: HttpClient) {}

  getSummary(): Observable<ReportSummary> {
    return this.http.get<ReportSummary>(`${this.api}/summary`);
  }
}
