import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IApprovalDTO } from '../../shared/model/approval-dto.model';
import {IMovementEntry, MovementEntry} from '../../shared/legacy/legacy-model/movement-entry.model';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';


type BooleanResponseType = HttpResponse<boolean>;
type EntityArrayResponseType = HttpResponse<MovementEntry[]>

@Injectable({ providedIn: 'root' })
export class MovementEntryApprovalService {
  public resourceUrlHR = '/api/attendance-mgt/movement-entry';
  public resourceUrlLM = '/api/common/movement-entry';

  constructor(private httpClient: HttpClient) {}

  getAllPendingHR(): Observable<HttpResponse<IMovementEntry[]>> {
    return this.httpClient.get<IMovementEntry[]>(this.resourceUrlHR + '/hr', { observe: 'response' })
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  approveAllHR(): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlHR + '/approve-all-hr', null, { observe: 'response' });
  }

  rejectAllHR(): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlHR + '/reject-all-hr', null, { observe: 'response' });
  }

  approveSelectedHR(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlHR + '/approve-selected-hr', approvalDTO, { observe: 'response' });
  }

  rejectSelectedHR(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlHR + '/reject-selected-hr', approvalDTO, { observe: 'response' });
  }

  getAllPendingLM(): Observable<HttpResponse<IMovementEntry[]>> {
    return this.httpClient.get<IMovementEntry[]>(this.resourceUrlLM + '/lm', { observe: 'response' })
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  approveAllLM(): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlLM + '/approve-all-lm', null, { observe: 'response' });
  }

  rejectAllLM(): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlLM + '/reject-all-lm', null, { observe: 'response' });
  }

  approveSelectedLM(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlLM + '/approve-selected-lm', approvalDTO, { observe: 'response' });
  }

  rejectSelectedLM(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.httpClient.put<boolean>(this.resourceUrlLM + '/reject-selected-lm', approvalDTO, { observe: 'response' });
  }
/*
  protected convertDateFromServer(movementEntry: IMovementEntry): IMovementEntry {
    return {
      ...movementEntry,
      startDate: movementEntry.startDate ? dayjs(movementEntry.startDate) : undefined,
      endDate: movementEntry.endDate ? dayjs(movementEntry.endDate) : undefined,
    };
  }

  protected convertResponseArrayFromServer(res: HttpResponse<IMovementEntry[]>): HttpResponse<IMovementEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }*/
/*  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfSubmission = res.body.dateOfSubmission ? dayjs(res.body.dateOfSubmission) : undefined;
      res.body.receivedAt = res.body.receivedAt ? dayjs(res.body.receivedAt) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }*/

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((movementEntry: IMovementEntry) => {
        movementEntry.startDate = movementEntry.startDate ? dayjs(movementEntry.startDate) : undefined;
        movementEntry.endDate = movementEntry.endDate ? dayjs(movementEntry.endDate) : undefined;
      });
    }
    return res;
  }
}
