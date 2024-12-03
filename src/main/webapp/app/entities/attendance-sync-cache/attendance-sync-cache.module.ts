import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AttendanceSyncCacheComponent } from './list/attendance-sync-cache.component';
import { AttendanceSyncCacheDetailComponent } from './detail/attendance-sync-cache-detail.component';
import { AttendanceSyncCacheUpdateComponent } from './update/attendance-sync-cache-update.component';
import { AttendanceSyncCacheDeleteDialogComponent } from './delete/attendance-sync-cache-delete-dialog.component';
import { AttendanceSyncCacheRoutingModule } from './route/attendance-sync-cache-routing.module';

@NgModule({
  imports: [SharedModule, AttendanceSyncCacheRoutingModule],
  declarations: [
    AttendanceSyncCacheComponent,
    AttendanceSyncCacheDetailComponent,
    AttendanceSyncCacheUpdateComponent,
    AttendanceSyncCacheDeleteDialogComponent,
  ],
})
export class AttendanceSyncCacheModule {}
