import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { EmployeeDetailModalComponent } from './employee-detail-modal.component';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  selector: 'jhi-onboard',
  templateUrl: './onboard.component.html',
  styleUrls: ['onboard.component.scss'],
})
export class OnboardComponent implements OnInit, OnDestroy {
  employees?: IEmployee[];
  eventSubscriber?: Subscription;

  constructor(protected employeeService: EmployeeService, protected eventManager: EventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEmployees();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmployee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmployees(): void {
    this.eventSubscriber = this.eventManager.subscribe('employeeListModification', () => this.loadAll());
  }

  details(employee: IEmployee): void {
    const modalRef = this.modalService.open(EmployeeDetailModalComponent, {
      scrollable: true,
      centered: true,
      size: 'xl',
      backdrop: true,
    });
    modalRef.componentInstance.employee = employee;
  }
}
