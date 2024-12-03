import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { FileTemplatesDetailComponent } from './file-templates-detail.component';
import { FileTemplatesDeleteDialogComponent } from './file-templates-delete-dialog.component';
import { fileTemplatesRoute } from './file-templates.route';
import { UserFileTemplatesComponent } from './user-end/user-file-templates.component';

import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(fileTemplatesRoute), BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [FileTemplatesDetailComponent, FileTemplatesDeleteDialogComponent, UserFileTemplatesComponent],
  entryComponents: [FileTemplatesDeleteDialogComponent],
})
export class BitsHrPayrollFileTemplatesModule {}
