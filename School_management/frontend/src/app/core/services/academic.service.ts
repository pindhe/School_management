import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AcademicYear,
  MessageResponse,
  SchoolClass,
  Subject,
  TeacherAssignment,
  TimetableEntry,
} from '../models/models';

@Injectable({ providedIn: 'root' })
export class AcademicService {
  private readonly baseUrl = `${environment.apiUrl}/academic`;

  constructor(private http: HttpClient) {}

  // Academic years
  listYears(): Observable<AcademicYear[]> {
    return this.http.get<AcademicYear[]>(`${this.baseUrl}/years`);
  }
  createYear(payload: unknown): Observable<AcademicYear> {
    return this.http.post<AcademicYear>(`${this.baseUrl}/years`, payload);
  }

  // Subjects
  listSubjects(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.baseUrl}/subjects`);
  }
  createSubject(payload: unknown): Observable<Subject> {
    return this.http.post<Subject>(`${this.baseUrl}/subjects`, payload);
  }

  // Classes
  listClasses(): Observable<SchoolClass[]> {
    return this.http.get<SchoolClass[]>(`${this.baseUrl}/classes`);
  }
  getClass(id: number): Observable<SchoolClass> {
    return this.http.get<SchoolClass>(`${this.baseUrl}/classes/${id}`);
  }
  createClass(payload: unknown): Observable<SchoolClass> {
    return this.http.post<SchoolClass>(`${this.baseUrl}/classes`, payload);
  }
  assignSubjects(classId: number, subjectIds: number[]): Observable<SchoolClass> {
    return this.http.post<SchoolClass>(`${this.baseUrl}/classes/${classId}/subjects`, {
      subjectIds,
    });
  }

  // Teacher assignments
  listAssignments(): Observable<TeacherAssignment[]> {
    return this.http.get<TeacherAssignment[]>(`${this.baseUrl}/assignments`);
  }
  assignmentsForTeacher(teacherId: number): Observable<TeacherAssignment[]> {
    return this.http.get<TeacherAssignment[]>(`${this.baseUrl}/assignments/teacher/${teacherId}`);
  }
  assignTeacher(payload: unknown): Observable<TeacherAssignment> {
    return this.http.post<TeacherAssignment>(`${this.baseUrl}/assignments`, payload);
  }

  // Enrollment
  enroll(studentId: number, classId: number): Observable<MessageResponse> {
    return this.http.post<MessageResponse>(`${this.baseUrl}/enroll`, { studentId, classId });
  }

  // Timetables
  createTimetable(payload: unknown): Observable<TimetableEntry> {
    return this.http.post<TimetableEntry>(`${this.baseUrl}/timetables`, payload);
  }
  timetableForClass(classId: number): Observable<TimetableEntry[]> {
    return this.http.get<TimetableEntry[]>(`${this.baseUrl}/timetables/class/${classId}`);
  }
  timetableForTeacher(teacherId: number): Observable<TimetableEntry[]> {
    return this.http.get<TimetableEntry[]>(`${this.baseUrl}/timetables/teacher/${teacherId}`);
  }
}
