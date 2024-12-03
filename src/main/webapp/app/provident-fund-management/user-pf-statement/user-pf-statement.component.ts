import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IUserPfStatement } from '../../shared/model/user-pf/user-pf-statement.model';
import { UserPfStatementService } from './user-pf-statement.service';
import dayjs from 'dayjs/esm';
import { OrganizationFilesUrl } from '../../shared/constants/organization-files-url';

@Component({
  selector: 'jhi-user-pf-statement',
  templateUrl: './user-pf-statement.component.html',
  styleUrls: ['user-pf-statement.component.scss'],
})
export class UserPfStatementComponent implements OnInit {
  userPfStatement!: IUserPfStatement;
  selectedDate = new Date();
  date = new FormControl(dayjs());

  previousYear = this.selectedDate.getFullYear() - 1;
  closingBalanceDate = new Date(this.previousYear, 11, 31);

  organizationFullName = '';
  organizationShortName = '';
  financeManagerName = '';
  financeManagerDesignation = '';
  financeManagerUnit = '';

  constructor(protected activatedRoute: ActivatedRoute, protected userPfStatementService: UserPfStatementService) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }

    if (sessionStorage.getItem('organizationShortName')) {
      this.organizationShortName = sessionStorage.getItem('organizationShortName')!;
    }

    if (sessionStorage.getItem('financeManagerName')) {
      this.financeManagerName = sessionStorage.getItem('financeManagerName')!;
    }

    if (sessionStorage.getItem('financeManagerDesignation')) {
      this.financeManagerDesignation = sessionStorage.getItem('financeManagerDesignation')!;
    }

    if (sessionStorage.getItem('financeManagerUnit')) {
      this.financeManagerUnit = sessionStorage.getItem('financeManagerUnit')!;
    }
  }

  ngOnInit(): void {}

  public downloadAsPDF(): void {
    window.print();
  }

  onClickShowPfStatement(): void {
    if (this.date.value !== null) {
      this.selectedDate = this.date.value.toDate();
    }
    this.previousYear = dayjs(this.selectedDate, 'DD/MM/YYYY').year() - 1;
    this.closingBalanceDate = new Date(this.previousYear, 11, 31);

    this.userPfStatementService.generateUserPfStatement(this.date.value).subscribe(res => {
      this.userPfStatement = res.body!;
    });
  }

  getPfStatementLetterHead(): string {
    return OrganizationFilesUrl.PF_STATEMENT_LETTER_HEAD;
  }

  getFinanceManagerSignature(): string {
    const resourceUrl = SERVER_API_URL + '/files/organizations/finance-manager-signature';
    return resourceUrl;
  }

  toDayjs(date: Date): dayjs.Dayjs {
    return dayjs(date);
  }
}
