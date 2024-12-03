import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IMovementEntry } from '../legacy-model/movement-entry.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from '../../../config/input.constants';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IMovementEntry>;
type EntityArrayResponseType = HttpResponse<IMovementEntry[]>;

@Injectable({ providedIn: 'root' })
export class MovementEntryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/movement-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(movementEntry: IMovementEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movementEntry);
    return this.http
      .post<IMovementEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  applyAndApproveMovementEntryByHR(movementEntry: IMovementEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movementEntry);
    return this.http
      .post<IMovementEntry>(this.resourceUrl + '/apply-approve-by-hr', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(movementEntry: IMovementEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movementEntry);
    return this.http
      .put<IMovementEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMovementEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMovementEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(movementEntry: IMovementEntry): IMovementEntry {
    const copy: IMovementEntry = Object.assign({}, movementEntry, {
      startDate: movementEntry.startDate && movementEntry.startDate.isValid() ? movementEntry.startDate.format(DATE_FORMAT) : undefined,
      startTime: movementEntry.startTime && movementEntry.startTime.isValid() ? movementEntry.startTime.toJSON() : undefined,
      endDate: movementEntry.endDate && movementEntry.endDate.isValid() ? movementEntry.endDate.format(DATE_FORMAT) : undefined,
      endTime: movementEntry.endTime && movementEntry.endTime.isValid() ? movementEntry.endTime.toJSON() : undefined,
      createdAt: movementEntry.createdAt && movementEntry.createdAt.isValid() ? movementEntry.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt: movementEntry.updatedAt && movementEntry.updatedAt.isValid() ? movementEntry.updatedAt.format(DATE_FORMAT) : undefined,
      sanctionAt: movementEntry.sanctionAt && movementEntry.sanctionAt.isValid() ? movementEntry.sanctionAt.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.startTime = res.body.startTime ? dayjs(res.body.startTime) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.endTime = res.body.endTime ? dayjs(res.body.endTime) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.sanctionAt = res.body.sanctionAt ? dayjs(res.body.sanctionAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((movementEntry: IMovementEntry) => {
        movementEntry.startDate = movementEntry.startDate ? dayjs(movementEntry.startDate) : undefined;
        movementEntry.startTime = movementEntry.startTime ? dayjs(movementEntry.startTime) : undefined;
        movementEntry.endDate = movementEntry.endDate ? dayjs(movementEntry.endDate) : undefined;
        movementEntry.endTime = movementEntry.endTime ? dayjs(movementEntry.endTime) : undefined;
        movementEntry.createdAt = movementEntry.createdAt ? dayjs(movementEntry.createdAt) : undefined;
        movementEntry.updatedAt = movementEntry.updatedAt ? dayjs(movementEntry.updatedAt) : undefined;
        movementEntry.sanctionAt = movementEntry.sanctionAt ? dayjs(movementEntry.sanctionAt) : undefined;
      });
    }
    return res;
  }
}
