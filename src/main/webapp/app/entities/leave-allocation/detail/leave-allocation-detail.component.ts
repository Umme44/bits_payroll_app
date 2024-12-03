import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaveAllocation } from '../leave-allocation.model';

@Component({
  selector: 'jhi-leave-allocation-detail',
  templateUrl: './leave-allocation-detail.component.html',
})
export class LeaveAllocationDetailComponent implements OnInit {
  leaveAllocation: ILeaveAllocation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveAllocation }) => {
      this.leaveAllocation = leaveAllocation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
