import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProcReqMaster, NewProcReqMaster } from '../proc-req-master.model';
import { swalClose, swalOnLoading } from '../../../shared/swal-common/swal-common';

export type PartialUpdateProcReqMaster = Partial<IProcReqMaster> & Pick<IProcReqMaster, 'id'>;

type RestOf<T extends IProcReqMaster | NewProcReqMaster> = Omit<
  T,
  | 'requestedDate'
  | 'expectedReceivedDate'
  | 'recommendationAt01'
  | 'recommendationAt02'
  | 'recommendationAt03'
  | 'recommendationAt04'
  | 'recommendationAt05'
  | 'rejectedDate'
  | 'closedAt'
  | 'createdAt'
  | 'updatedAt'
> & {
  requestedDate?: string | null;
  expectedReceivedDate?: string | null;
  recommendationAt01?: string | null;
  recommendationAt02?: string | null;
  recommendationAt03?: string | null;
  recommendationAt04?: string | null;
  recommendationAt05?: string | null;
  rejectedDate?: string | null;
  closedAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestProcReqMaster = RestOf<IProcReqMaster>;

export type NewRestProcReqMaster = RestOf<NewProcReqMaster>;

export type PartialUpdateRestProcReqMaster = RestOf<PartialUpdateProcReqMaster>;

export type EntityResponseType = HttpResponse<IProcReqMaster>;
export type EntityArrayResponseType = HttpResponse<IProcReqMaster[]>;

@Injectable({ providedIn: 'root' })
export class ProcReqMasterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/procurement-mgt/proc-req-masters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(procReqMaster: NewProcReqMaster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procReqMaster);
    return this.http
      .post<RestProcReqMaster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(procReqMaster: IProcReqMaster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procReqMaster);
    return this.http
      .put<RestProcReqMaster>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(procReqMaster: PartialUpdateProcReqMaster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procReqMaster);
    return this.http
      .patch<RestProcReqMaster>(`${this.resourceUrl}/${this.getProcReqMasterIdentifier(procReqMaster)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProcReqMaster>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProcReqMaster[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProcReqMasterIdentifier(procReqMaster: Pick<IProcReqMaster, 'id'>): number {
    return procReqMaster.id;
  }

  compareProcReqMaster(o1: Pick<IProcReqMaster, 'id'> | null, o2: Pick<IProcReqMaster, 'id'> | null): boolean {
    return o1 && o2 ? this.getProcReqMasterIdentifier(o1) === this.getProcReqMasterIdentifier(o2) : o1 === o2;
  }

  addProcReqMasterToCollectionIfMissing<Type extends Pick<IProcReqMaster, 'id'>>(
    procReqMasterCollection: Type[],
    ...procReqMastersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const procReqMasters: Type[] = procReqMastersToCheck.filter(isPresent);
    if (procReqMasters.length > 0) {
      const procReqMasterCollectionIdentifiers = procReqMasterCollection.map(
        procReqMasterItem => this.getProcReqMasterIdentifier(procReqMasterItem)!
      );
      const procReqMastersToAdd = procReqMasters.filter(procReqMasterItem => {
        const procReqMasterIdentifier = this.getProcReqMasterIdentifier(procReqMasterItem);
        if (procReqMasterCollectionIdentifiers.includes(procReqMasterIdentifier)) {
          return false;
        }
        procReqMasterCollectionIdentifiers.push(procReqMasterIdentifier);
        return true;
      });
      return [...procReqMastersToAdd, ...procReqMasterCollection];
    }
    return procReqMasterCollection;
  }

  protected convertDateFromClient<T extends IProcReqMaster | NewProcReqMaster | PartialUpdateProcReqMaster>(procReqMaster: T): RestOf<T> {
    return {
      ...procReqMaster,
      requestedDate: procReqMaster.requestedDate?.format(DATE_FORMAT) ?? null,
      expectedReceivedDate: procReqMaster.expectedReceivedDate?.format(DATE_FORMAT) ?? null,
      recommendationAt01: procReqMaster.recommendationAt01?.toJSON() ?? null,
      recommendationAt02: procReqMaster.recommendationAt02?.toJSON() ?? null,
      recommendationAt03: procReqMaster.recommendationAt03?.toJSON() ?? null,
      recommendationAt04: procReqMaster.recommendationAt04?.toJSON() ?? null,
      recommendationAt05: procReqMaster.recommendationAt05?.toJSON() ?? null,
      rejectedDate: procReqMaster.rejectedDate?.format(DATE_FORMAT) ?? null,
      closedAt: procReqMaster.closedAt?.toJSON() ?? null,
      createdAt: procReqMaster.createdAt?.toJSON() ?? null,
      updatedAt: procReqMaster.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProcReqMaster: RestProcReqMaster): IProcReqMaster {
    return {
      ...restProcReqMaster,
      requestedDate: restProcReqMaster.requestedDate ? dayjs(restProcReqMaster.requestedDate) : undefined,
      expectedReceivedDate: restProcReqMaster.expectedReceivedDate ? dayjs(restProcReqMaster.expectedReceivedDate) : undefined,
      recommendationAt01: restProcReqMaster.recommendationAt01 ? dayjs(restProcReqMaster.recommendationAt01) : undefined,
      recommendationAt02: restProcReqMaster.recommendationAt02 ? dayjs(restProcReqMaster.recommendationAt02) : undefined,
      recommendationAt03: restProcReqMaster.recommendationAt03 ? dayjs(restProcReqMaster.recommendationAt03) : undefined,
      recommendationAt04: restProcReqMaster.recommendationAt04 ? dayjs(restProcReqMaster.recommendationAt04) : undefined,
      recommendationAt05: restProcReqMaster.recommendationAt05 ? dayjs(restProcReqMaster.recommendationAt05) : undefined,
      rejectedDate: restProcReqMaster.rejectedDate ? dayjs(restProcReqMaster.rejectedDate) : undefined,
      closedAt: restProcReqMaster.closedAt ? dayjs(restProcReqMaster.closedAt) : undefined,
      createdAt: restProcReqMaster.createdAt ? dayjs(restProcReqMaster.createdAt) : undefined,
      updatedAt: restProcReqMaster.updatedAt ? dayjs(restProcReqMaster.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProcReqMaster>): HttpResponse<IProcReqMaster> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProcReqMaster[]>): HttpResponse<IProcReqMaster[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  close(id: number): Observable<boolean> {
    return this.http.put<boolean>(this.resourceUrl + '/close/' + id, { observe: 'response' });
  }

  downloadFile(id: number): void {
    swalOnLoading('Preparing for download...');
    this.downloadFileCommonUser(id).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should

        const newBlob = new Blob([x.body!], { type: 'application/octet-stream' });
        const fileName = this.getFileName(x.headers.get('content-disposition')!);

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob, fileName);
          return;
        }*/

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = fileName;
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        // tslint:disable-next-line:typedef
        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      },
      err => {},
      () => {
        swalClose();
      }
    );
  }

  downloadFileCommonUser(id: number): Observable<HttpResponse<Blob>> {
    const uri = this.resourceUrl + '/download/' + id;
    return this.http.get<Blob>(uri, { responseType: 'blob' as 'json', observe: 'response' });
  }

  private getFileName(disposition: string): string {
    const utf8FilenameRegex = /filename\*=UTF-8''([\w%\-\\.]+)(?:; ?|$)/i;
    const asciiFilenameRegex = /^filename=(["']?)(.*?[^\\])\1(?:; ?|$)/i;

    let fileName = '';
    if (utf8FilenameRegex.test(disposition)) {
      fileName = decodeURIComponent(utf8FilenameRegex.exec(disposition)![1]);
    } else {
      // prevent ReDos attacks by anchoring the ascii regex to string start and
      //  slicing off everything before 'filename='
      const filenameStart = disposition.toLowerCase().indexOf('filename=');
      if (filenameStart >= 0) {
        const partialDisposition = disposition.slice(filenameStart);
        const matches = asciiFilenameRegex.exec(partialDisposition);
        if (matches != null && matches[2]) {
          fileName = matches[2];
        }
      }
    }
    return fileName;
  }
}
