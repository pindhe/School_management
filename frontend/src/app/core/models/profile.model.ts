import { RoleName } from './auth.model';

export interface Profile {
  id: number;
  username: string;
  email: string;
  role: RoleName;
  enabled: boolean;
  createdAt?: string;
  studentsManaged: number;
}
