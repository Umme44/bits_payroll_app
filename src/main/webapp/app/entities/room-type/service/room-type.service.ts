import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomType, NewRoomType } from '../room-type.model';

export type PartialUpdateRoomType = Partial<IRoomType> & Pick<IRoomType, 'id'>;

type RestOf<T extends IRoomType | NewRoomType> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestRoomType = RestOf<IRoomType>;

export type NewRestRoomType = RestOf<NewRoomType>;

export type PartialUpdateRestRoomType = RestOf<PartialUpdateRoomType>;

export type EntityResponseType = HttpResponse<IRoomType>;
export type EntityArrayResponseType = HttpResponse<IRoomType[]>;

@Injectable({ providedIn: 'root' })
export class RoomTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/room-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(roomType: NewRoomType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomType);
    return this.http
      .post<RestRoomType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(roomType: IRoomType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomType);
    return this.http
      .put<RestRoomType>(`${this.resourceUrl}/${this.getRoomTypeIdentifier(roomType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(roomType: PartialUpdateRoomType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomType);
    return this.http
      .patch<RestRoomType>(`${this.resourceUrl}/${this.getRoomTypeIdentifier(roomType)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRoomType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRoomType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoomTypeIdentifier(roomType: Pick<IRoomType, 'id'>): number {
    return roomType.id;
  }

  compareRoomType(o1: Pick<IRoomType, 'id'> | null, o2: Pick<IRoomType, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoomTypeIdentifier(o1) === this.getRoomTypeIdentifier(o2) : o1 === o2;
  }

  addRoomTypeToCollectionIfMissing<Type extends Pick<IRoomType, 'id'>>(
    roomTypeCollection: Type[],
    ...roomTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roomTypes: Type[] = roomTypesToCheck.filter(isPresent);
    if (roomTypes.length > 0) {
      const roomTypeCollectionIdentifiers = roomTypeCollection.map(roomTypeItem => this.getRoomTypeIdentifier(roomTypeItem)!);
      const roomTypesToAdd = roomTypes.filter(roomTypeItem => {
        const roomTypeIdentifier = this.getRoomTypeIdentifier(roomTypeItem);
        if (roomTypeCollectionIdentifiers.includes(roomTypeIdentifier)) {
          return false;
        }
        roomTypeCollectionIdentifiers.push(roomTypeIdentifier);
        return true;
      });
      return [...roomTypesToAdd, ...roomTypeCollection];
    }
    return roomTypeCollection;
  }

  protected convertDateFromClient<T extends IRoomType | NewRoomType | PartialUpdateRoomType>(roomType: T): RestOf<T> {
    return {
      ...roomType,
      createdAt: roomType.createdAt?.toJSON() ?? null,
      updatedAt: roomType.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRoomType: RestRoomType): IRoomType {
    return {
      ...restRoomType,
      createdAt: restRoomType.createdAt ? dayjs(restRoomType.createdAt) : undefined,
      updatedAt: restRoomType.updatedAt ? dayjs(restRoomType.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRoomType>): HttpResponse<IRoomType> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRoomType[]>): HttpResponse<IRoomType[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
