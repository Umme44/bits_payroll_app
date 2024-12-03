import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DefinedKeys } from '../../../shared/constants/defined-keys.constant';
import { swalSuccessWithMessage } from '../../../shared/swal-common/swal-common';
import { ConfigService } from '../../config/service/config.service';

@Component({
  selector: 'jhi-config-update',
  templateUrl: './prf-configuration-update.component.html',
})
export class PrfConfigurationUpdateComponent implements OnInit {
  prfOfficerPinFormControl = new FormControl(null, Validators.required);
  prfTeamContactNoFormControl = new FormControl(null, [Validators.required, Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]);
  prfMaxTotalApproximateAmountFormControl = new FormControl(null, [Validators.min(1), Validators.required]);
  procurementApprovalFlow = new FormControl(null, [Validators.required]);

  constructor(protected configService: ConfigService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.loadPRFOfficerEmployeePin();
    this.loadPRFTeamContactNo();
    this.loadPRFMaxApproxAmount();
    this.loadPRFApprovalFlow();
  }

  loadPRFOfficerEmployeePin(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_OFFICER_EMPLOYEE_PIN).subscribe(res => {
      this.prfOfficerPinFormControl.setValue(res.body?.value);
    });
  }

  loadPRFTeamContactNo(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_TEAM_CONTACT_NO).subscribe(res => {
      this.prfTeamContactNoFormControl.setValue(res.body?.value);
    });
  }

  loadPRFMaxApproxAmount(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT).subscribe(res => {
      this.prfMaxTotalApproximateAmountFormControl.setValue(Number(res.body?.value));
    });
  }

  loadPRFApprovalFlow(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_APPROVAL_FLOW).subscribe(res => {
      this.procurementApprovalFlow.setValue(res.body?.value);
    });
  }

  updatePRFOfficerPIN(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_OFFICER_EMPLOYEE_PIN).subscribe(configResponse => {
      const config = configResponse.body!;
      config.value = this.prfOfficerPinFormControl.value;
      this.configService.updateByProcurementOfficer(config).subscribe(successResponse => {
        swalSuccessWithMessage('Saved!');
      });
    });
  }

  updatePRFTeamContactNumber(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_TEAM_CONTACT_NO).subscribe(configResponse => {
      const config = configResponse.body!;
      config.value = this.prfTeamContactNoFormControl.value;
      this.configService.updateByProcurementOfficer(config).subscribe(successResponse => {
        swalSuccessWithMessage('Saved!');
      });
    });
  }

  updatePRFMaxApproxAmount(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT).subscribe(configResponse => {
      const config = configResponse.body!;
      config.value = this.prfMaxTotalApproximateAmountFormControl.value;
      this.configService.updateByProcurementOfficer(config).subscribe(successResponse => {
        swalSuccessWithMessage('Saved!');
      });
    });
  }

  updatePRFApprovalFlow(): void {
    this.configService.findByKeyByProcurementOfficer(DefinedKeys.PRF_APPROVAL_FLOW).subscribe(configResponse => {
      const config = configResponse.body!;
      config.value = this.procurementApprovalFlow.value;
      this.configService.updateByProcurementOfficer(config).subscribe(successResponse => {
        swalSuccessWithMessage('Saved!');
      });
    });
  }

  changePRFOfficerPin(employee: any | undefined | null): void {
    this.prfOfficerPinFormControl.setValue(employee?.pin);
  }
}
