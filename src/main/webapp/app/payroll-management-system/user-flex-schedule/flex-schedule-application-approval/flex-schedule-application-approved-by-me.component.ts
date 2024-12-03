import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FlexScheduleApplicationApprovalService } from './flex-schedule-application-approval.service';

import { Status } from '../../../shared/model/enumerations/status.model';
import { IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';

@Component({
  selector: 'jhi-flex-schedule-application-approved-by-me',
  templateUrl: './flex-schedule-application-approved-by-me.component.html',
})
export class FlexScheduleApplicationApprovedByMeComponent implements OnInit {
  pageType!: string;
  listOfIds: number[] = [];

  flexScheduleApplications: IFlexScheduleApplication[] = [];

  status!: Status;
  employees: IEmployee[] = [];

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected flexScheduleApplicationApprovalService: FlexScheduleApplicationApprovalService,
    protected modalService: NgbModal
  ) {
    this.pageType = activatedRoute.snapshot.params['pageType'];
  }

  ngOnInit(): void {
    this.flexScheduleApplicationApprovalService.findAllApprovedByLM().subscribe(res => {
      this.flexScheduleApplications = res.body!;
    });
    this.loadAllEmployeeList();
  }

  getProfilePicture(pin: String): String {
    return SERVER_API_URL + '/files/get-employees-image/' + pin;
  }

  loadAllEmployeeList(): void {
    this.flexScheduleApplicationApprovalService.findEmployeeListOfFlexScheduleApprovedByMe().subscribe(res => {
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
    });
  }

  onChangeSelectEmployee($event: any): void {
    this.flexScheduleApplicationApprovalService.findAllApprovedByLM($event.id).subscribe(res => {
      this.flexScheduleApplications = res.body!;
    });
  }

  // openView(flexScheduleApplication: IFlexScheduleApplication): void {
  //   const modalRef = this.modalService.open(FlexScheduleApplicationDetailApprovalModalComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.flexScheduleApplication = flexScheduleApplication;
  //   modalRef.componentInstance.modalType = 'approvedByMe';
  // }
}
