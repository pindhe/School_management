import { Student } from './student.model';

export interface DashboardStats {
  totalStudents: number;
  studentsWithPhoto: number;
  studentsWithoutPhoto: number;
  totalUsers: number;
  adminUsers: number;
  guestUsers: number;
  recentStudents: Student[];
}
