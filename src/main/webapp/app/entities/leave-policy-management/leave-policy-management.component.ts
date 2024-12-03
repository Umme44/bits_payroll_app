import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import  { LeavePolicyManagement } from './leave-policy-management';
import {
  flexScheduleApplicationUserRoute
} from "../../payroll-management-system/user-flex-schedule/flex-schedule-application-user/flex-schedule-application-user.route";


@Component({
  selector: 'jhi-leave-policy-management',
  templateUrl: './leave-policy-management.component.html',
  standalone: true,
  styleUrls: ['./leave-policy-management.component.scss'],
  imports: [FormsModule,ReactiveFormsModule,CommonModule]
})
export class LeavePolicyManagementComponent implements OnInit {
  hasMonthlyMaxLimit: boolean = false;
  encashmentCriteria : boolean = false;
  isHalfDayAllowed : boolean = false;
  isAutoDeductible: boolean = false;
  hasExpiry: boolean = false;
  hasAllocationCriteria :boolean = false;
  isEncashableEachYear: boolean = false;
  isForwardable:boolean = false;
  allocationType:string = '';
  hasLifeCycleLimit:boolean = false;
  selectedRequirementDocument: string = '';
  minDurationForSupportingDocumentInDays :number;
  selectedRequirement:string = '';
  selectedCalenderYearAdvance: string = '';
  hasAutoAllocationFromJoining :boolean = false;
  perCalenderYearAllocation: number | null = null;
  calenderYearAdvancedB:boolean = false;
  calenderMonthAdvancedB:boolean = false;
  perDayFixedFactorB:boolean = false;
  calenderYearAdvance :string;
  requiredAllocationFactor:string;
  selectedAllocationTypes: string[] = ['Calendar Year Advance', 'Calendar Month Advance', 'Per Day Fixed Factor'];
  gender:string[] = ['All','Male','Female'];
  matitalStatus:string[] = ['All','Married','Unmarried'];
  requirementForSupportingDocument:string[] = ['Not Required','Strictly Required','Duration Based'];





  leavePolicyForm: FormGroup;

  constructor(
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {

  }

  ngOnInit(): void {

  }

  onSubmit() {
    this.leavePolicyForm.valid;
    // if (this.leavePolicyForm.valid) {
    //
    //   const formValue: LeavePolicyManagement = this.leavePolicyForm.value;
    //
    //   console.log('Form Submitted:', formValue);
    // } else {
    //   alert('Please fill in all required fields!');
    // }
  }
}


