import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { DefinedKeys } from '../../shared/constants/defined-keys.constant';
import { swalForWarningWithMessage, swalSuccessWithMessage } from '../../shared/swal-common/swal-common';
import { IConfig, NewConfig } from '../config/config.model';
import { ConfigService } from '../config/service/config.service';

@Component({
  selector: 'jhi-config-update',
  templateUrl: './config-custom-update.component.html',
})
export class ConfigCustomUpdateComponent implements OnInit {
  maxDaysForChangeAttendanceStatusFormControl = new FormControl(null, [Validators.min(1)]);
  maxDaysForAttendanceDataLoadFormControl = new FormControl(null, [Validators.min(30)]);

  maxAllowedDayForChangeAttendanceStatusConfig!: IConfig;
  maxDaysForAttendanceDataLoadConfig!: IConfig;

  isLeaveApplicationEnabled = false;
  isIncomeTaxStatementVisibilityOn = false;

  leaveApplicationEnableConfig!: IConfig;
  isIncomeTaxStatementVisibilityConfig!: IConfig;

  isRRFEnabled = false;
  rrfEnableConfig!: IConfig | NewConfig;

  isNomineeNIDVerificationEnabled = false;
  isPFNomineeNIDVerificationEnabled = false;
  GFisNomineeNIDVerificationEnabled = false;


  nomineeNidVerificationEnableConfig!: IConfig | NewConfig;
  pFNomineeNidVerificationEnableConfig!: IConfig | NewConfig;
  gFNomineeNidVerificationEnableConfig!: IConfig | NewConfig;

  constructor(protected configService: ConfigService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {
    this.isRRFEnabled = false;
    this.isNomineeNIDVerificationEnabled = false;
    this.isPFNomineeNIDVerificationEnabled = false;
    this.GFisNomineeNIDVerificationEnabled = false;

    this.rrfEnableConfig = { id: null, key: DefinedKeys.is_rrf_enabled_for_user_end, value: 'FALSE' };
    this.nomineeNidVerificationEnableConfig = { id: null, key: DefinedKeys.is_nominee_nid_verification_enabled_for_user_end, value: 'FALSE' };
    this.pFNomineeNidVerificationEnableConfig = { id: null, key: DefinedKeys.is_pf_nominee_nid_verification_enabled_for_user_end, value: 'FALSE' };
    this.gFNomineeNidVerificationEnableConfig = { id: null, key: DefinedKeys.is_gf_nominee_nid_verification_enabled_for_user_end, value: 'FALSE' };
  }

  ngOnInit(): void {
    this.loadIsLeaveApplicationEnabled();
    this.loadMaxAllowedDaysForChangeStatus();
    this.loadMaxDaysForAttendanceLoad();
    this.loadIncomeTaxStatementVisibilityConfig();
    this.loadRRFConfigEnabled();
    this.loadIsNomineeNIDVerificationEnabled();
    this.loadIsPFNomineeNIDVerificationEnabled();
    this.loadIsGFNomineeNIDVerificationEnabled();
  }

  loadRRFConfigEnabled(): void {
    this.configService.findByKey(DefinedKeys.is_rrf_enabled_for_user_end).subscribe(res => {
      this.rrfEnableConfig = res.body!;
      if (this.rrfEnableConfig.value === 'TRUE') {
        this.isRRFEnabled = true;
      } else {
        this.isRRFEnabled = false;
      }
    });
  }

  loadIsNomineeNIDVerificationEnabled(): void {
    this.configService.findByKey(DefinedKeys.is_nominee_nid_verification_enabled_for_user_end).subscribe(res => {
      this.nomineeNidVerificationEnableConfig = res.body!;
      if (this.nomineeNidVerificationEnableConfig.value === 'TRUE') {
        this.isNomineeNIDVerificationEnabled = true;
      } else {
        this.isNomineeNIDVerificationEnabled = false;
      }
    });
  }
  loadIsPFNomineeNIDVerificationEnabled(): void {
    this.configService.findByKey(DefinedKeys.is_pf_nominee_nid_verification_enabled_for_user_end).subscribe(res => {
      this.pFNomineeNidVerificationEnableConfig = res.body!;
      if (this.pFNomineeNidVerificationEnableConfig.value === 'TRUE') {
        this.isPFNomineeNIDVerificationEnabled = true;
      } else {
        this.isPFNomineeNIDVerificationEnabled = false;
      }
    });
  }
  loadIsGFNomineeNIDVerificationEnabled(): void {
    this.configService.findByKey(DefinedKeys.is_gf_nominee_nid_verification_enabled_for_user_end).subscribe(res => {
      this.gFNomineeNidVerificationEnableConfig = res.body!;
      if (this.gFNomineeNidVerificationEnableConfig.value === 'TRUE') {
        this.GFisNomineeNIDVerificationEnabled = true;
      } else {
        this.GFisNomineeNIDVerificationEnabled = false;
      }
    });
  }


  loadIsLeaveApplicationEnabled(): void {
    this.configService.findByKey(DefinedKeys.is_leave_application_enabled_for_user_end).subscribe(res => {
      this.leaveApplicationEnableConfig = res.body!;
      if (this.leaveApplicationEnableConfig.value === 'TRUE') this.isLeaveApplicationEnabled = true;
      else this.isLeaveApplicationEnabled = false;
    });
  }

  loadMaxAllowedDaysForChangeStatus(): void {
    this.configService.findByKey(DefinedKeys.max_allowed_previous_days_for_change_attendance_status).subscribe(res => {
      this.maxAllowedDayForChangeAttendanceStatusConfig = res.body!;
      this.maxDaysForChangeAttendanceStatusFormControl.setValue(this.maxAllowedDayForChangeAttendanceStatusConfig.value);
    });
  }

  loadMaxDaysForAttendanceLoad(): void {
    this.configService.findByKey(DefinedKeys.max_duration_in_days_for_attendance_data_load).subscribe(res => {
      this.maxDaysForAttendanceDataLoadConfig = res.body!;
      this.maxDaysForAttendanceDataLoadFormControl.setValue(this.maxDaysForAttendanceDataLoadConfig.value);
    });
  }

  loadIncomeTaxStatementVisibilityConfig(): void {
    this.configService.findByKey(DefinedKeys.is_income_tax_visibility_enabled_for_user_end).subscribe(res => {
      this.isIncomeTaxStatementVisibilityConfig = res.body!;
      if (this.isIncomeTaxStatementVisibilityConfig.value === 'TRUE') {
        this.isIncomeTaxStatementVisibilityOn = true;
      } else {
        this.isIncomeTaxStatementVisibilityOn = false;
      }
    });
  }

  changeFlagOfUserLeaveApplication(): void {
    if (this.isLeaveApplicationEnabled) this.leaveApplicationEnableConfig.value = 'FALSE';
    else this.leaveApplicationEnableConfig.value = 'TRUE';
    this.configService.update(this.leaveApplicationEnableConfig).subscribe(res => {
      this.leaveApplicationEnableConfig = res.body!;
      this.isLeaveApplicationEnabled = !this.isLeaveApplicationEnabled;
      if (this.isLeaveApplicationEnabled) swalSuccessWithMessage('Leave Application has enabled!');
      else swalForWarningWithMessage('Leave Application has disabled!');
    });
  }

  changeMaxAllowedDaysChangeAttendanceStatus(): void {
    this.maxAllowedDayForChangeAttendanceStatusConfig.value = this.maxDaysForChangeAttendanceStatusFormControl.value;
    this.configService.update(this.maxAllowedDayForChangeAttendanceStatusConfig).subscribe(res => {
      this.maxAllowedDayForChangeAttendanceStatusConfig = res.body!;
      swalSuccessWithMessage('Saved!');
    });
  }

  changeMaxDaysForAttendanceDataLoad(): void {
    this.maxDaysForAttendanceDataLoadConfig.value = this.maxDaysForAttendanceDataLoadFormControl.value;
    this.configService.update(this.maxDaysForAttendanceDataLoadConfig).subscribe(res => {
      this.maxDaysForAttendanceDataLoadConfig = res.body!;
      swalSuccessWithMessage('Saved!');
    });
  }

  changeFlagOfIncomeTaxStatementVisibility(): void {
    if (this.isIncomeTaxStatementVisibilityOn) this.isIncomeTaxStatementVisibilityConfig.value = 'FALSE';
    else this.isIncomeTaxStatementVisibilityConfig.value = 'TRUE';

    this.configService.update(this.isIncomeTaxStatementVisibilityConfig).subscribe(res => {
      this.isIncomeTaxStatementVisibilityConfig = res.body!;
      this.isIncomeTaxStatementVisibilityOn = !this.isIncomeTaxStatementVisibilityOn;
      if (this.isIncomeTaxStatementVisibilityOn) swalSuccessWithMessage('Successfully enabled income tax statement visibility!');
      else swalForWarningWithMessage('Successfully disabled income tax statement visibility!');
    });
  }

  changeFlagOfRRF(): void {
    if (this.isRRFEnabled) this.rrfEnableConfig.value = 'FALSE';
    else this.rrfEnableConfig.value = 'TRUE';

    this.configService.update(this.rrfEnableConfig).subscribe(res => {
      this.rrfEnableConfig = res.body!;

      this.isRRFEnabled = !this.isRRFEnabled;
      if (this.isRRFEnabled) swalSuccessWithMessage('Successfully enabled recruitment requisition form!');
      else swalForWarningWithMessage('Successfully disabled recruitment requisition form!');
    }, err => {
      this.configService.create(this.rrfEnableConfig as NewConfig).subscribe(res => {
        this.rrfEnableConfig = res.body!;

        this.isRRFEnabled = !this.rrfEnableConfig;
        if (this.isRRFEnabled) swalSuccessWithMessage('Successfully enabled recruitment requisition form!');
        else swalForWarningWithMessage('Successfully disabled recruitment requisition form!');
      });
    });
  }

  changeFlagOfNomineeNIDVerification(): void {
    if (this.isNomineeNIDVerificationEnabled) this.nomineeNidVerificationEnableConfig.value = 'FALSE';
    else this.nomineeNidVerificationEnableConfig.value = 'TRUE';

    this.configService.update(this.nomineeNidVerificationEnableConfig).subscribe(
      res => {
        this.nomineeNidVerificationEnableConfig = res.body!;

        this.isNomineeNIDVerificationEnabled = !this.isNomineeNIDVerificationEnabled;
        if (this.isNomineeNIDVerificationEnabled) swalSuccessWithMessage('Successfully enabled Nominee NID Verification!');
        else swalForWarningWithMessage('Successfully disabled Nominee NID Verification!');
      },
      err => {
        this.configService.create(this.nomineeNidVerificationEnableConfig as NewConfig).subscribe(res => {
          this.nomineeNidVerificationEnableConfig = res.body!;

          this.isNomineeNIDVerificationEnabled = !this.isNomineeNIDVerificationEnabled;
          if (this.isNomineeNIDVerificationEnabled) swalSuccessWithMessage('Successfully enabled Nominee NID Verification!');
          else swalForWarningWithMessage('Successfully disabled Nominee NID Verification!');
        });
      }
    );
  }
  changeFlagOfPFNomineeNIDVerification(): void {
    if (this.isPFNomineeNIDVerificationEnabled) this.pFNomineeNidVerificationEnableConfig.value = 'FALSE';
    else this.pFNomineeNidVerificationEnableConfig.value = 'TRUE';

    this.configService.update(this.pFNomineeNidVerificationEnableConfig).subscribe(
      res => {
        this.pFNomineeNidVerificationEnableConfig = res.body!;

        this.isPFNomineeNIDVerificationEnabled = !this.isPFNomineeNIDVerificationEnabled;
        if (this.isPFNomineeNIDVerificationEnabled) swalSuccessWithMessage('Successfully enabled PF Nominee NID Verification!');
        else swalForWarningWithMessage('Successfully disabled PF Nominee NID Verification!');
      },
      err => {
        this.configService.create(this.pFNomineeNidVerificationEnableConfig as NewConfig).subscribe(res => {
          this.pFNomineeNidVerificationEnableConfig = res.body!;

          this.isPFNomineeNIDVerificationEnabled = !this.isPFNomineeNIDVerificationEnabled;
          if (this.isPFNomineeNIDVerificationEnabled) swalSuccessWithMessage('Successfully enabled PF Nominee NID Verification!');
          else swalForWarningWithMessage('Successfully disabled PF Nominee NID Verification!');
        });
      }
    );
  }
  changeFlagOfGFNomineeNIDVerification(): void {
    if (this.GFisNomineeNIDVerificationEnabled) this.gFNomineeNidVerificationEnableConfig.value = 'FALSE';
    else this.gFNomineeNidVerificationEnableConfig.value = 'TRUE';

    this.configService.update(this.gFNomineeNidVerificationEnableConfig).subscribe(
      res => {
        this.gFNomineeNidVerificationEnableConfig = res.body!;

        this.GFisNomineeNIDVerificationEnabled = !this.GFisNomineeNIDVerificationEnabled;
        if (this.GFisNomineeNIDVerificationEnabled) swalSuccessWithMessage('Successfully enabled GF Nominee NID Verification!');
        else swalForWarningWithMessage('Successfully disabled GF Nominee NID Verification!');
      },
      err => {
        this.configService.create(this.gFNomineeNidVerificationEnableConfig as NewConfig).subscribe(res => {
          this.gFNomineeNidVerificationEnableConfig = res.body!;

          this.GFisNomineeNIDVerificationEnabled = !this.GFisNomineeNIDVerificationEnabled;
          if (this.GFisNomineeNIDVerificationEnabled) swalSuccessWithMessage('Successfully enabled GF Nominee NID Verification!');
          else swalForWarningWithMessage('Successfully disabled GF Nominee NID Verification!');
        });
      }
    );
  }
}
