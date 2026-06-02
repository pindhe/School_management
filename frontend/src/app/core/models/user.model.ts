import { RoleName } from './auth.model';

export interface AppUserSummary {
  id: number;
  username: string;
  email: string;
  role: RoleName;
  enabled: boolean;
  createdAt?: string;
}
