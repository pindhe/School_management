export interface DomainCount {
  domain: string;
  count: number;
}

export interface ReportSummary {
  totalStudents: number;
  studentsWithPhoto: number;
  photoCoveragePercent: number;
  totalUsers: number;
  adminUsers: number;
  guestUsers: number;
  studentsByEmailDomain: DomainCount[];
}
