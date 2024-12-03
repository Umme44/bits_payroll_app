import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FileTemplatesComponent } from './list/file-templates.component';
import { FileTemplatesDetailComponent } from './detail/file-templates-detail.component';
import { FileTemplatesUpdateComponent } from './update/file-templates-update.component';
import { FileTemplatesDeleteDialogComponent } from './delete/file-templates-delete-dialog.component';
import { FileTemplatesRoutingModule } from './route/file-templates-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [SharedModule, FileTemplatesRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [FileTemplatesComponent, FileTemplatesDetailComponent, FileTemplatesUpdateComponent, FileTemplatesDeleteDialogComponent],
})
export class FileTemplatesModule {}
