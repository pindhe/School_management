import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { GradeRecord, MessageResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class GradeService {
  private readonly baseUrl = `${environment.apiUrl}/grades`;

  constructor(private http: HttpClient) {}

  record(payload: unknown): Observable<GradeRecord> {
    return this.http.post<GradeRecord>(this.baseUrl, payload);
  }

  delete(id: number): Observable<MessageResponse> {
    return this.http.delete<MessageResponse>(`${this.baseUrl}/${id}`);
  }

  studentGrades(studentId: number): Observable<GradeRecord[]> {
    return this.http.get<GradeRecord[]>(`${this.baseUrl}/student/${studentId}`);
  }

  myGrades(): Observable<GradeRecord[]> {
    return this.http.get<GradeRecord[]>(`${this.baseUrl}/me`);
  }
}
