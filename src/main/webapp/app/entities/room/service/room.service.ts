import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoom, NewRoom } from '../room.model';

export type PartialUpdateRoom = Partial<IRoom> & Pick<IRoom, 'id'>;

type RestOf<T extends IRoom | NewRoom> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestRoom = RestOf<IRoom>;

export type NewRestRoom = RestOf<NewRoom>;

export type PartialUpdateRestRoom = RestOf<PartialUpdateRoom>;

export type EntityResponseType = HttpResponse<IRoom>;
export type EntityArrayResponseType = HttpResponse<IRoom[]>;

@Injectable({ providedIn: 'root' })
export class RoomService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rooms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(room: NewRoom): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(room);
    return this.http.post<RestRoom>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(room: IRoom): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(room);
    return this.http
      .put<RestRoom>(`${this.resourceUrl}/${this.getRoomIdentifier(room)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(room: PartialUpdateRoom): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(room);
    return this.http
      .patch<RestRoom>(`${this.resourceUrl}/${this.getRoomIdentifier(room)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRoom>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRoom[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoomIdentifier(room: Pick<IRoom, 'id'>): number {
    return room.id;
  }

  compareRoom(o1: Pick<IRoom, 'id'> | null, o2: Pick<IRoom, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoomIdentifier(o1) === this.getRoomIdentifier(o2) : o1 === o2;
  }

  addRoomToCollectionIfMissing<Type extends Pick<IRoom, 'id'>>(
    roomCollection: Type[],
    ...roomsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rooms: Type[] = roomsToCheck.filter(isPresent);
    if (rooms.length > 0) {
      const roomCollectionIdentifiers = roomCollection.map(roomItem => this.getRoomIdentifier(roomItem)!);
      const roomsToAdd = rooms.filter(roomItem => {
        const roomIdentifier = this.getRoomIdentifier(roomItem);
        if (roomCollectionIdentifiers.includes(roomIdentifier)) {
          return false;
        }
        roomCollectionIdentifiers.push(roomIdentifier);
        return true;
      });
      return [...roomsToAdd, ...roomCollection];
    }
    return roomCollection;
  }

  protected convertDateFromClient<T extends IRoom | NewRoom | PartialUpdateRoom>(room: T): RestOf<T> {
    return {
      ...room,
      createdAt: room.createdAt?.toJSON() ?? null,
      updatedAt: room.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRoom: RestRoom): IRoom {
    return {
      ...restRoom,
      createdAt: restRoom.createdAt ? dayjs(restRoom.createdAt) : undefined,
      updatedAt: restRoom.updatedAt ? dayjs(restRoom.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRoom>): HttpResponse<IRoom> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRoom[]>): HttpResponse<IRoom[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
