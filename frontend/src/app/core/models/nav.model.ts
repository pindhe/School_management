export interface NavItem {
  label: string;
  icon: string;
  route: string;
  adminOnly?: boolean;
  exact?: boolean;
}
