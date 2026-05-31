export type RoleName = 'ADMIN' | 'TEACHER' | 'STUDENT' | 'PARENT';
export type Gender = 'MALE' | 'FEMALE' | 'OTHER';
export type AttendanceStatus = 'PRESENT' | 'ABSENT' | 'LATE' | 'LEAVE';
export type TargetRole = 'ALL' | 'ADMIN' | 'TEACHER' | 'STUDENT' | 'PARENT';
export type DayOfWeek =
  | 'MONDAY'
  | 'TUESDAY'
  | 'WEDNESDAY'
  | 'THURSDAY'
  | 'FRIDAY'
  | 'SATURDAY'
  | 'SUNDAY';

export interface UserResponse {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  dateOfBirth?: string;
  address?: string;
  phoneNumber?: string;
  gender?: Gender;
  role: RoleName;
  active: boolean;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  user: UserResponse;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

export interface TeacherResponse {
  id: number;
  user: UserResponse;
  employeeId: string;
  department?: string;
  hireDate?: string;
}

export interface StudentResponse {
  id: number;
  user: UserResponse;
  admissionNumber: string;
  currentClassId?: number;
  currentClassName?: string;
  dateOfAdmission?: string;
}

export interface ParentResponse {
  id: number;
  user: UserResponse;
  children: StudentResponse[];
}

export interface AcademicYear {
  id: number;
  yearStart: number;
  yearEnd: number;
  name: string;
  active: boolean;
}

export interface Subject {
  id: number;
  name: string;
  code: string;
  description?: string;
}

export interface SchoolClass {
  id: number;
  name: string;
  academicYearId: number;
  academicYearName: string;
  homeroomTeacherId?: number;
  homeroomTeacherName?: string;
  subjects: Subject[];
}

export interface TeacherAssignment {
  id: number;
  teacherId: number;
  teacherName: string;
  classId: number;
  className: string;
  subjectId: number;
  subjectName: string;
  academicYearId: number;
  academicYearName: string;
}

export interface TimetableEntry {
  id: number;
  classId: number;
  className: string;
  subjectId: number;
  subjectName: string;
  teacherId: number;
  teacherName: string;
  dayOfWeek: DayOfWeek;
  startTime: string;
  endTime: string;
  roomNumber?: string;
  academicYearId: number;
}

export interface AttendanceRecord {
  id: number;
  studentId: number;
  studentName: string;
  date: string;
  status: AttendanceStatus;
  remarks?: string;
  recordedByTeacherName?: string;
}

export interface GradeRecord {
  id: number;
  studentId: number;
  studentName: string;
  subjectId: number;
  subjectName: string;
  academicYearId: number;
  academicYearName: string;
  gradeType: string;
  score: number;
  maxScore: number;
  percentage: number;
  letterGrade: string;
  gradedByTeacherName?: string;
  gradeDate: string;
  remarks?: string;
}

export interface Announcement {
  id: number;
  title: string;
  content: string;
  publishedByName: string;
  publishDate: string;
  targetRole: TargetRole;
  targetClassId?: number;
  targetClassName?: string;
  active: boolean;
}

export interface MessageResponse {
  message: string;
}
