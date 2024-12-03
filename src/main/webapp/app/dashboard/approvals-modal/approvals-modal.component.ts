import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DashboardService } from '../dashboard.service';
import { Router } from '@angular/router';
import { INotification } from '../../shared/model/notification.model';
import { NotificationService } from '../../layouts/navbar/notification.service';

@Component({
  selector: 'jhi-approvals-modal',
  templateUrl: './approvals-modal.component.html',
  styleUrls: ['./approval-modal.component.scss', './../dashboard.scss'],
})
export class ApprovalsModalComponent implements OnInit {
  haveIAnySubordinate = false;
  notification!: INotification;

  constructor(
    protected activeModal: NgbActiveModal,
    protected dashboardService: DashboardService,
    protected router: Router,
    protected notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.anySubOrdinate();
    this.loadNotification();
  }

  loadNotification(): void {
    this.notificationService.getApprovalNotificationsCommon().subscribe((res: HttpResponse<INotification>) => {
      this.notification = res.body!;
    });
  }

  anySubOrdinate(): void {
    this.dashboardService.anySubOrdinate().subscribe(res => {
      if (res.body && res.body === true) {
        this.haveIAnySubordinate = true;
      }
    });
  }

  close(): void {
    this.activeModal.close('Close click');
  }

  goToApprovals(routerLink: string, limitToSubOrdinate = true): void {
    if (limitToSubOrdinate) {
      if (this.haveIAnySubordinate) {
        this.close();
        this.router.navigate([routerLink]);
      }
    } else {
      this.close();
      this.router.navigate([routerLink]);
    }
  }

  dismiss(): void {
    this.activeModal.dismiss('Cross click');
  }
}
