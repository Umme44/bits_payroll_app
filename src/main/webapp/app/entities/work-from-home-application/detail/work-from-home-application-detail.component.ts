import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {IWorkFromHomeApplication} from "../work-from-home-application.model";

@Component({
  selector: 'jhi-work-from-home-application-detail',
  templateUrl: './work-from-home-application-detail.component.html',
})
export class WorkFromHomeApplicationDetailComponent implements OnInit {
  workFromHomeApplication: IWorkFromHomeApplication | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workFromHomeApplication }) => (this.workFromHomeApplication = workFromHomeApplication));
  }

  previousState(): void {
    window.history.back();
  }

  public getUserFriendlyAttendanceStatus(status: any): string {
    if (status === 'APPROVED') return 'Approved';
    if (status === 'NOT_APPROVED') return 'Not Approved';
    if (status === 'PENDING') return 'Pending';
    return status;
  }
}
