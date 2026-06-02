export interface Student {
  id: number;
  name: string;
  address: string;
  phone: string;
  email: string;
  photoUrl?: string | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface StudentRequest {
  name: string;
  address: string;
  phone: string;
  email: string;
}
