import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventType } from '../../shared/model/enumerations/event-type.model';
import { EmploymentHistoriesService } from './employment-histories.service';
import { FormBuilder, Validators } from '@angular/forms';
import { IEmploymentHistory } from '../../shared/legacy/legacy-model/employment-history.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { EventManager } from '../../core/util/event-manager.service';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';

@Component({
  selector: 'jhi-employment-history',
  templateUrl: './employment-histories.component.html',
  styleUrls: ['employment-histories.component.scss'],
})
export class EmploymentHistoriesComponent implements OnInit, OnDestroy {
  incrementEventType = EventType.INCREMENT;
  promotionEventType = EventType.PROMOTION;
  transferEventType = EventType.TRANSFER;
  joiningEventType = EventType.JOIN;
  confirmationEventType = EventType.CONFIRM;
  resignationEventType = EventType.RESIGNATION;

  employmentHistories?: IEmploymentHistory[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  // employeeIdForm!: FormGroup;
  selectedId!: number;

  selectedEmployee: IEmployee | null = null;

  pin?: string;
  employeeName?: string;

  employees: IEmployee[] = [];

  employeeIdForm = this.fb.group({
    employeeId: [null, Validators.required],
  });

  constructor(
    protected employmentHistoriesService: EmploymentHistoriesService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  load(): void {
    let employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    if (employeeId === undefined) {
      employeeId = this.selectedId;
    }
    if (employeeId !== undefined && employeeId !== null) {
      this.employmentHistoriesService.getEmploymentHistoryByEmployeeID(employeeId).subscribe((res: HttpResponse<IEmploymentHistory[]>) => {
        this.employmentHistories = res.body || [];
        if (this.employmentHistories?.length > 0) {
          this.pin = this.employmentHistories[0].pin;
          this.employeeName = this.employmentHistories[0].employeeName;
        }
      });
    }
    this.registerChangeInEmploymentHistories();
  }

  ngOnInit(): void {
    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => {
      this.employees = res.body || [];
      this.employees = this.employees.map(item => {
        return {
          id: item.id,
          pin: item.pin,
          name: item.fullName,
          designation: item.designationName,
          fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
        };
      });
      const id = this.activatedRoute.snapshot.params['employeeId'];
      if (id !== undefined) {
        this.selectedId = id;
        this.load();
      }
    });

    this.registerChangeInEmploymentHistories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmploymentHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  registerChangeInEmploymentHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('employmentHistoryListModification', () => this.load());
  }
}
