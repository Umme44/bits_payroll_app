import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomRequisition, NewRoomRequisition } from '../room-requisition.model';

export type PartialUpdateRoomRequisition = Partial<IRoomRequisition> & Pick<IRoomRequisition, 'id'>;

type RestOf<T extends IRoomRequisition | NewRoomRequisition> = Omit<
  T,
  'createdAt' | 'updatedAt' | 'sanctionedAt' | 'bookingStartDate' | 'bookingEndDate'
> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  sanctionedAt?: string | null;
  bookingStartDate?: string | null;
  bookingEndDate?: string | null;
};

export type RestRoomRequisition = RestOf<IRoomRequisition>;

export type NewRestRoomRequisition = RestOf<NewRoomRequisition>;

export type PartialUpdateRestRoomRequisition = RestOf<PartialUpdateRoomRequisition>;

export type EntityResponseType = HttpResponse<IRoomRequisition>;
export type EntityArrayResponseType = HttpResponse<IRoomRequisition[]>;

@Injectable({ providedIn: 'root' })
export class RoomRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/room-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(roomRequisition: NewRoomRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomRequisition);
    return this.http
      .post<RestRoomRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(roomRequisition: IRoomRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomRequisition);
    return this.http
      .put<RestRoomRequisition>(`${this.resourceUrl}/${this.getRoomRequisitionIdentifier(roomRequisition)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(roomRequisition: PartialUpdateRoomRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomRequisition);
    return this.http
      .patch<RestRoomRequisition>(`${this.resourceUrl}/${this.getRoomRequisitionIdentifier(roomRequisition)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRoomRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRoomRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoomRequisitionIdentifier(roomRequisition: Pick<IRoomRequisition, 'id'>): number {
    return roomRequisition.id;
  }

  compareRoomRequisition(o1: Pick<IRoomRequisition, 'id'> | null, o2: Pick<IRoomRequisition, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoomRequisitionIdentifier(o1) === this.getRoomRequisitionIdentifier(o2) : o1 === o2;
  }

  addRoomRequisitionToCollectionIfMissing<Type extends Pick<IRoomRequisition, 'id'>>(
    roomRequisitionCollection: Type[],
    ...roomRequisitionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roomRequisitions: Type[] = roomRequisitionsToCheck.filter(isPresent);
    if (roomRequisitions.length > 0) {
      const roomRequisitionCollectionIdentifiers = roomRequisitionCollection.map(
        roomRequisitionItem => this.getRoomRequisitionIdentifier(roomRequisitionItem)!
      );
      const roomRequisitionsToAdd = roomRequisitions.filter(roomRequisitionItem => {
        const roomRequisitionIdentifier = this.getRoomRequisitionIdentifier(roomRequisitionItem);
        if (roomRequisitionCollectionIdentifiers.includes(roomRequisitionIdentifier)) {
          return false;
        }
        roomRequisitionCollectionIdentifiers.push(roomRequisitionIdentifier);
        return true;
      });
      return [...roomRequisitionsToAdd, ...roomRequisitionCollection];
    }
    return roomRequisitionCollection;
  }

  protected convertDateFromClient<T extends IRoomRequisition | NewRoomRequisition | PartialUpdateRoomRequisition>(
    roomRequisition: T
  ): RestOf<T> {
    return {
      ...roomRequisition,
      createdAt: roomRequisition.createdAt?.toJSON() ?? null,
      updatedAt: roomRequisition.updatedAt?.toJSON() ?? null,
      sanctionedAt: roomRequisition.sanctionedAt?.toJSON() ?? null,
      bookingStartDate: roomRequisition.bookingStartDate?.format(DATE_FORMAT) ?? null,
      bookingEndDate: roomRequisition.bookingEndDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRoomRequisition: RestRoomRequisition): IRoomRequisition {
    return {
      ...restRoomRequisition,
      createdAt: restRoomRequisition.createdAt ? dayjs(restRoomRequisition.createdAt) : undefined,
      updatedAt: restRoomRequisition.updatedAt ? dayjs(restRoomRequisition.updatedAt) : undefined,
      sanctionedAt: restRoomRequisition.sanctionedAt ? dayjs(restRoomRequisition.sanctionedAt) : undefined,
      bookingStartDate: restRoomRequisition.bookingStartDate ? dayjs(restRoomRequisition.bookingStartDate) : undefined,
      bookingEndDate: restRoomRequisition.bookingEndDate ? dayjs(restRoomRequisition.bookingEndDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRoomRequisition>): HttpResponse<IRoomRequisition> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRoomRequisition[]>): HttpResponse<IRoomRequisition[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
