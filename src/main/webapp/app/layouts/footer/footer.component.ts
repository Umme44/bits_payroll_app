import { Component } from '@angular/core';

import { OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { IOrganization } from '../../shared/legacy/legacy-model/organization.model';
import { OrganizationCustomService } from '../../shared/legacy/legacy-service/organization-custom.service';
@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.scss'],
})
export class FooterComponent implements OnInit {
  organization!: IOrganization;
  copyWriteYear = new Date().getFullYear();

  constructor(protected organizationService: OrganizationCustomService, private accountService: AccountService) {}

  ngOnInit(): void {
    if (this.accountService.isAuthenticated()) {
      this.organizationService.getBasicDetails().subscribe(res => {
        this.organization = res.body!;
        sessionStorage.setItem('organizationShortName', this.organization.shortName!);
        sessionStorage.setItem('organizationFullName', this.organization.fullName!);
        sessionStorage.setItem('financeManagerName', this.organization.financeManagerName!);
        sessionStorage.setItem('financeManagerDesignation', this.organization.financeManagerDesignation!);
        sessionStorage.setItem('financeManagerUnit', this.organization.financeManagerUnit!);

      });
    }
  }
}
