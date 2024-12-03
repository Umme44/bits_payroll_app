import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUserWorkFromHomeApplication } from '../user-work-from-home-application.model';

@Component({
  selector: 'jhi-user-work-from-home-application-detail',
  templateUrl: './user-work-from-home-application-detail.component.html',
})
export class UserWorkFromHomeApplicationDetailComponent implements OnInit {
  workFromHomeApplication: IUserWorkFromHomeApplication | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workFromHomeApplication }) => (this.workFromHomeApplication = workFromHomeApplication));
  }

  previousState(): void {
    window.history.back();
  }
}
