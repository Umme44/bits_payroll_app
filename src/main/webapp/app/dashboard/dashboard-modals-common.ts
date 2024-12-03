import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

export class DashboardModalsCommon {
  constructor(protected activeModal: NgbActiveModal, private router: Router) {}

  close(): void {
    this.activeModal.close('Close click');
  }

  routerNavigation(routerLink: string): void {
    this.close();
    this.router.navigate([routerLink]);
  }

  dismiss(): void {
    this.activeModal.dismiss('Cross click');
  }
}
