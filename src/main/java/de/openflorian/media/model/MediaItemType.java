package de.openflorian.media.model;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Media Item Type<br/>
 * <br/>
 * Available media item types:
 * - LocalImage<br/>
 * - RemoteImage<br/>
 * - LocalVideo<br/>
 * - YoutubeVideo<br/>
 * - VimeoVideo<br/>
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public enum MediaItemType {
	LocalImage,
	RemoteImage,
	LocalVideo,
	YoutubeVideo,
	VimeoVideo
}
