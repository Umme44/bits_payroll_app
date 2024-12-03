import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-employment-actions',
  templateUrl: './employment-actions.component.html',
  styleUrls: ['employment-actions.component.scss'],
})
export class EmploymentActionsComponent implements OnInit {
  eventSubscriber?: Subscription;

  constructor() {}

  ngOnInit(): void {}
}
