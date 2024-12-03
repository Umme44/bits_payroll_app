import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAitConfig } from '../ait-config.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ait-config-detail',
  templateUrl: './ait-config-detail.component.html',
})
export class AitConfigDetailComponent implements OnInit {
  aitConfig: IAitConfig | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aitConfig }) => {
      this.aitConfig = aitConfig;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
