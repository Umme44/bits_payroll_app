import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import {IUserPfStatement} from "../../../shared/model/user-pf/user-pf-statement.model";
import {PfAccountService} from "../service/pf-account.service";
import {OrganizationFilesUrl} from "../../../shared/constants/organization-files-url";
import dayjs from 'dayjs/esm';


@Component({
  selector: 'jhi-pf-statement',
  templateUrl: './pf-statement.component.html',
  styleUrls: ['pf-statement.component.scss'],
})
export class PfStatementComponent implements OnInit {
  userPfStatement!: IUserPfStatement;
  selectedDate = dayjs(new Date());

  date = new FormControl(dayjs());

  previousYear = this.selectedDate.subtract(1, 'year').year();
  closingBalanceDate = new Date(this.previousYear, 11, 31);

  pfAccountId!: number;

  organizationFullName = '';
  organizationShortName = '';
  financeManagerName = '';
  financeManagerDesignation = '';
  financeManagerUnit = '';

  constructor(protected activatedRoute: ActivatedRoute, protected pfAccountService: PfAccountService) {
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

  ngOnInit(): void {
    this.pfAccountId = this.activatedRoute.snapshot.params['id'];
    this.pfAccountService.generateUserPfStatement(this.pfAccountId, this.date.value).subscribe(res => {
      this.userPfStatement = res.body!;
    });
  }

  public downloadAsPDF(): void {
    window.print();
  }

  onClickShowPfStatement(): void {
    this.selectedDate = this.date.value;
    this.previousYear = dayjs(this.selectedDate, 'DD/MM/YYYY').year() - 1;
    this.closingBalanceDate = new Date(this.previousYear, 11, 31);

    this.pfAccountService.generateUserPfStatement(this.pfAccountId, this.date.value).subscribe(res => {
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
}
