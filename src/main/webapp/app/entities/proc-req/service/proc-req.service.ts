import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IProcReqMaster } from '../../proc-req-master/proc-req-master.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';

type EntityResponseType = HttpResponse<IProcReqMaster>;
type EntityArrayResponseType = HttpResponse<IProcReqMaster[]>;
type BooleanResponseType = HttpResponse<boolean>;

@Injectable({ providedIn: 'root' })
export class ProcReqService {
  public resourceUrl = SERVER_API_URL + 'api/common/proc-req-user';

  public approvalResourceUrl = SERVER_API_URL + 'api/common/prf/approval';

  constructor(protected http: HttpClient) {}

  create(procReqMaster: IProcReqMaster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procReqMaster);
    return this.http
      .post<IProcReqMaster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(procReqMaster: IProcReqMaster): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procReqMaster);
    return this.http
      .put<IProcReqMaster>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProcReqMaster>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    Object.keys(req).forEach(k => req[k] == null && delete req[k]);
    const options = createRequestOption(req);
    return this.http
      .get<IProcReqMaster[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  downloadFileCommonUser(id: number): Observable<HttpResponse<Blob>> {
    const uri = this.resourceUrl + '/download/' + id;
    return this.http.get<Blob>(uri, { responseType: 'blob' as 'json', observe: 'response' });
  }

  protected convertDateFromClient(procReqMaster: IProcReqMaster): IProcReqMaster {
    const copy: IProcReqMaster = Object.assign({}, procReqMaster, {
      expectedReceivedDate:
        procReqMaster.expectedReceivedDate && procReqMaster.expectedReceivedDate.isValid()
          ? procReqMaster.expectedReceivedDate.format(DATE_FORMAT)
          : undefined,
      requestedDate:
        procReqMaster.requestedDate && procReqMaster.requestedDate.isValid() ? procReqMaster.requestedDate.format(DATE_FORMAT) : undefined,
      createdAt: procReqMaster.createdAt && procReqMaster.createdAt.isValid() ? procReqMaster.createdAt.toJSON() : undefined,
      updatedAt: procReqMaster.updatedAt && procReqMaster.updatedAt.isValid() ? procReqMaster.updatedAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expectedReceivedDate = res.body.expectedReceivedDate ? dayjs(res.body.expectedReceivedDate) : undefined;
      res.body.requestedDate = res.body.requestedDate ? dayjs(res.body.requestedDate) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  // protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
  //   if (res.body) {
  //     res.body.forEach((procReqMaster: IProcReqMaster) => {
  //       procReqMaster.expectedReceivedDate = procReqMaster.expectedReceivedDate ? dayjs(procReqMaster.expectedReceivedDate) : undefined;
  //       procReqMaster.requestedDate = procReqMaster.requestedDate ? dayjs(procReqMaster.requestedDate) : undefined;
  //       procReqMaster.createdAt = procReqMaster.createdAt ? dayjs(procReqMaster.createdAt) : undefined;
  //       procReqMaster.updatedAt = procReqMaster.updatedAt ? dayjs(procReqMaster.updatedAt) : undefined;
  //     });
  //   }
  //   return res;
  // }

  findAllPendingLM(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IProcReqMaster[]>(this.approvalResourceUrl + '/pending-list', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  approvedByDepartmentHead(isCTOApprovalRequired: boolean, listOfIds: number[]): Observable<BooleanResponseType> {
    const approvalDTO = { ...new ApprovalDTO(), listOfIds };
    return this.http.put<boolean>(
      `${this.approvalResourceUrl}/approve-by-dept-head?isCTOApprovalRequired=` + isCTOApprovalRequired,
      approvalDTO,
      {
        observe: 'response',
      }
    );
  }

  approveSelected(listOfIds: number[]): Observable<BooleanResponseType> {
    const approvalDTO = { ...new ApprovalDTO(), listOfIds };
    return this.http.put<boolean>(`${this.approvalResourceUrl}/approve-selected`, approvalDTO, { observe: 'response' });
  }

  rejectSelected(procReqMaster: IProcReqMaster): Observable<BooleanResponseType> {
    return this.http.put<boolean>(`${this.approvalResourceUrl}/reject-selected`, procReqMaster, { observe: 'response' });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((procReqMaster: IProcReqMaster) => {
        procReqMaster.requestedDate = procReqMaster.requestedDate ? dayjs(procReqMaster.requestedDate) : undefined;
        procReqMaster.expectedReceivedDate = procReqMaster.expectedReceivedDate ? dayjs(procReqMaster.expectedReceivedDate) : undefined;
        procReqMaster.recommendationAt01 = procReqMaster.recommendationAt01 ? dayjs(procReqMaster.recommendationAt01) : undefined;
        procReqMaster.recommendationAt02 = procReqMaster.recommendationAt02 ? dayjs(procReqMaster.recommendationAt02) : undefined;
        procReqMaster.recommendationAt03 = procReqMaster.recommendationAt03 ? dayjs(procReqMaster.recommendationAt03) : undefined;
        procReqMaster.recommendationAt04 = procReqMaster.recommendationAt04 ? dayjs(procReqMaster.recommendationAt04) : undefined;
        procReqMaster.recommendationAt05 = procReqMaster.recommendationAt05 ? dayjs(procReqMaster.recommendationAt05) : undefined;
        procReqMaster.rejectedDate = procReqMaster.rejectedDate ? dayjs(procReqMaster.rejectedDate) : undefined;
        procReqMaster.createdAt = procReqMaster.createdAt ? dayjs(procReqMaster.createdAt) : undefined;
        procReqMaster.updatedAt = procReqMaster.updatedAt ? dayjs(procReqMaster.updatedAt) : undefined;
      });
    }
    return res;
  }
}
