import { Component, OnInit } from '@angular/core';
@Component({
  selector: 'jhi-pf-loan-approval-form',
  templateUrl: './pf-loan-application-approved-form.component.html',
})
export class PfLoanApplicationApprovedFormComponent implements OnInit {
  organizationFullName = '';
  constructor() {
    if (sessionStorage.getItem('organizationShortName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }
  ngOnInit(): void {}
}
