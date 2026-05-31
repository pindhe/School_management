import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  MessageResponse,
  ParentResponse,
  StudentResponse,
  TeacherResponse,
  UserResponse,
} from '../models/models';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly baseUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  listUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.baseUrl);
  }

  setActive(userId: number, active: boolean): Observable<UserResponse> {
    return this.http.patch<UserResponse>(`${this.baseUrl}/${userId}/status?active=${active}`, {});
  }

  // Teachers
  listTeachers(): Observable<TeacherResponse[]> {
    return this.http.get<TeacherResponse[]>(`${this.baseUrl}/teachers`);
  }
  createTeacher(payload: unknown): Observable<TeacherResponse> {
    return this.http.post<TeacherResponse>(`${this.baseUrl}/teachers`, payload);
  }

  // Students
  listStudents(classId?: number): Observable<StudentResponse[]> {
    const query = classId ? `?classId=${classId}` : '';
    return this.http.get<StudentResponse[]>(`${this.baseUrl}/students${query}`);
  }
  createStudent(payload: unknown): Observable<StudentResponse> {
    return this.http.post<StudentResponse>(`${this.baseUrl}/students`, payload);
  }

  // Parents
  listParents(): Observable<ParentResponse[]> {
    return this.http.get<ParentResponse[]>(`${this.baseUrl}/parents`);
  }
  createParent(payload: unknown): Observable<ParentResponse> {
    return this.http.post<ParentResponse>(`${this.baseUrl}/parents`, payload);
  }
  getParent(id: number): Observable<ParentResponse> {
    return this.http.get<ParentResponse>(`${this.baseUrl}/parents/${id}`);
  }
  linkParent(payload: {
    studentId: number;
    parentId: number;
    relationship?: string;
  }): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/link-parent`, payload);
  }
}
