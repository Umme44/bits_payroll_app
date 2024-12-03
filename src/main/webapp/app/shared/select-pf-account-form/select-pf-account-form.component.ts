import { Component, OnInit, Input } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormGroup } from '@angular/forms';
import {IPfAccount} from "../legacy/legacy-model/pf-account.model";
import {PfAccountService} from "../legacy/legacy-service/pf-account.service";
import {IEmployee} from "../legacy/legacy-model/employee.model";
/* import { PfAccountService } from '../../entities/pf-account/pf-account.service'; */
/* import { IPfAccount } from '../model/pf-account.model'; */
/* import { IEmployee } from 'app/shared/model/employee.model'; */

@Component({
  selector: 'jhi-select-pf-account-form',
  templateUrl: './select-pf-account-form.component.html',
  styleUrls: ['./select-pf-account-form.component.scss'],
})
export class SelectPfAccountFormComponent implements OnInit {
  @Input()
  pfAccountIdForm!: FormGroup;

  @Input()
  selectedId!: number;

  selectedPfAccount: IPfAccount | null = null;
  pfAccounts!: IPfAccount[];

  constructor(private pfAccountService: PfAccountService) {}

  ngOnInit(): void {
    this.pfAccountService.getAllPfAccountsList().subscribe((response: HttpResponse<IPfAccount[]>) => {
      this.pfAccounts = response.body || [];
      this.pfAccounts = this.pfAccounts.map(pfAccount => {
        return {
          id: pfAccount.id,
          pin: pfAccount.pin,
          name: pfAccount.accHolderName,
          designation: pfAccount.designationName,
          accHolderName: pfAccount.pin + ' - ' + pfAccount.accHolderName + ' - ' + pfAccount.designationName,
        };
      });
    });
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  setPfAccount(id: number): void {
    this.selectedId = id;
    for (const pfAccount of this.pfAccounts) {
      if (pfAccount.id === id) {
        this.selectedPfAccount = pfAccount;
        return;
      }
    }
  }
}
