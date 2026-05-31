import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AttendanceRecord, AttendanceStatus } from '../models/models';

export interface AttendanceEntry {
  studentId: number;
  status: AttendanceStatus;
  remarks?: string;
}

@Injectable({ providedIn: 'root' })
export class AttendanceService {
  private readonly baseUrl = `${environment.apiUrl}/attendance`;

  constructor(private http: HttpClient) {}

  mark(date: string, entries: AttendanceEntry[]): Observable<AttendanceRecord[]> {
    return this.http.post<AttendanceRecord[]>(`${this.baseUrl}/mark`, { date, entries });
  }

  classAttendance(classId: number, date: string): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(`${this.baseUrl}/class/${classId}?date=${date}`);
  }

  studentAttendance(studentId: number): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(`${this.baseUrl}/student/${studentId}`);
  }

  myAttendance(): Observable<AttendanceRecord[]> {
    return this.http.get<AttendanceRecord[]>(`${this.baseUrl}/me`);
  }
}
