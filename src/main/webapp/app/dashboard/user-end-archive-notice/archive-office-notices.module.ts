import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { archiveOfficeNoticesRoute } from './archive-office-notices.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import {SharedModule} from "../../shared/shared.module";
import {ArchiveOfficeNoticesComponent} from "./archive-office-notices.component";


@NgModule({
  imports: [SharedModule, RouterModule.forChild(archiveOfficeNoticesRoute), BitsHrPayrollHeaderModule],
  declarations: [
    ArchiveOfficeNoticesComponent,
  ],
})
export class BitsHrPayrollArchiveOfficeNoticesModule {}
