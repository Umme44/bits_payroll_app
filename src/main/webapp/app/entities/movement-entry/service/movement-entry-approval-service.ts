import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IApprovalDTO } from '../../../shared/model/approval-dto.model';
import { IMovementEntry, NewMovementEntry } from '../movement-entry.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import { PartialUpdateMovementEntry } from './movement-entry.service';
import dayjs from 'dayjs/esm';

type RestOf<T extends IMovementEntry | NewMovementEntry> = Omit<
  T,
  'startDate' | 'startTime' | 'endDate' | 'endTime' | 'createdAt' | 'updatedAt' | 'sanctionAt'
> & {
  startDate?: string | null;
  startTime?: string | null;
  endDate?: string | null;
  endTime?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  sanctionAt?: string | null;
};

type BooleanResponseType = HttpResponse<boolean>;
export type RestMovementEntry = RestOf<IMovementEntry>;

@Injectable({ providedIn: 'root' })
export class MovementEntryApprovalService {
  public resourceUrlHR = '/api/attendance-mgt/movement-entry';
  public resourceUrlLM = '/api/common/movement-entry';

  constructor(private httpClient: HttpClient) {}

  getAllPendingHR(): Observable<HttpResponse<IMovementEntry[]>> {
    return this.httpClient
      .get<RestMovementEntry[]>(this.resourceUrlHR + '/hr', { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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
    return this.httpClient
      .get<RestMovementEntry[]>(this.resourceUrlLM + '/lm', { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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

  protected convertDateFromClient<T extends IMovementEntry | NewMovementEntry | PartialUpdateMovementEntry>(movementEntry: T): RestOf<T> {
    return {
      ...movementEntry,
      startDate: movementEntry.startDate?.format(DATE_FORMAT) ?? null,
      startTime: movementEntry.startTime?.toJSON() ?? null,
      endDate: movementEntry.endDate?.format(DATE_FORMAT) ?? null,
      endTime: movementEntry.endTime?.toJSON() ?? null,
      createdAt: movementEntry.createdAt?.format(DATE_FORMAT) ?? null,
      updatedAt: movementEntry.updatedAt?.format(DATE_FORMAT) ?? null,
      sanctionAt: movementEntry.sanctionAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMovementEntry: RestMovementEntry): IMovementEntry {
    return {
      ...restMovementEntry,
      startDate: restMovementEntry.startDate ? dayjs(restMovementEntry.startDate) : undefined,
      startTime: restMovementEntry.startTime ? dayjs(restMovementEntry.startTime) : undefined,
      endDate: restMovementEntry.endDate ? dayjs(restMovementEntry.endDate) : undefined,
      endTime: restMovementEntry.endTime ? dayjs(restMovementEntry.endTime) : undefined,
      createdAt: restMovementEntry.createdAt ? dayjs(restMovementEntry.createdAt) : undefined,
      updatedAt: restMovementEntry.updatedAt ? dayjs(restMovementEntry.updatedAt) : undefined,
      sanctionAt: restMovementEntry.sanctionAt ? dayjs(restMovementEntry.sanctionAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMovementEntry>): HttpResponse<IMovementEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMovementEntry[]>): HttpResponse<IMovementEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
